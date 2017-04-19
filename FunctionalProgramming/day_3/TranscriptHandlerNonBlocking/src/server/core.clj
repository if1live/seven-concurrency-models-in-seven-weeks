;; /translation/:n에 대한 GET 요청에 대해서 번역이 아직 준비되지 않았으면 블로킹하는 것이 아니라
; HTTP 409 상태 코드를 리턴하도록 번역 서버를 수정하라.

(ns server.core
  (:require [compojure.core     :refer :all]
            [compojure.handler  :refer [api]]
            [ring.util.response :refer [response status]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clojure.edn :as edn]
            [clj-http.client :as client]
            [server.sentences :refer :all]
            [server.charset :refer [wrap-charset]]))


(def snippets (repeatedly promise))

(def translator "http://localhost:3001/translate")

(defn translate [text]
  (future (:body (client/post translator {:body text}))))

(def translations
  (delay
   (map translate (strings->sentences (map deref snippets)))))

(defn accept-snippet [n text]
  (deliver (nth snippets n) text))

;; 해설
;; 1. return value
;; string -> text + 200 OK
;; nil -> 409 nil
;; 되도록 app-routes 변경
;;
;; 2. promise timeout
;; (deref ref timeout-ms timeout-val)
;; blocking 탈출하는 간단한 방법은 타임아웃 잡아서 종료시키는거
;;
;; 3. promise + future
;; promise deref할때 timeout을 걸어둔다
;; promise에 값을 보내기 위해서 future를 사용한다
;; future가 별로 쓰레드에서 돌아가면서 값을 promise에 채우려고 시도할거다
;; timeout 이내에 promise에 값을 채우지 못했다는건 translations이 아직 blocking 상태라는거
;; timeout 이내에 값을 채웠으면 채워진 값을 사용한다
(defn get-translation [n]
  (let [p (promise)]
    (future (deliver p @(nth @translations n)))
    (deref p 100 nil)))

(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
       (accept-snippet (edn/read-string n) (slurp body))
       (response "OK"))
  (GET "/translation/:n" [n]
       (let [retval (get-translation (edn/read-string n))]
         (if (nil? retval)
           (status {} 409)
           (response retval)))))

(defn -main [& args]
  (run-jetty (wrap-charset (api app-routes)) {:port 3000}))
