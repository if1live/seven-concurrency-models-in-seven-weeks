(ns server.core
  (:require [compojure.core     :refer :all]
            [compojure.handler  :refer [api]]
            [ring.util.response :refer [response]]
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

(defn get-translation [n]
  @(nth @translations n))

(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
       (accept-snippet (edn/read-string n) (slurp body))
       (response "OK"))
  (GET "/translation/:n" [n]
       (response (get-translation (edn/read-string n)))))

(defn -main [& args]
  (run-jetty (wrap-charset (api app-routes)) {:port 3000}))
