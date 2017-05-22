(ns server.core
  (:require [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clojure.edn :as edn]
            [clj-http.client :as client]
            [compojure.core :refer :all]
            [compojure.handler :refer [api]]
            [server.charset :refer [wrap-charset]]
            [server.translate :refer [translate]]
            [server.sentences :refer [strings->sentences]]
            [server.session :refer [new-session get-session]]))

(defn create-session []
  (let [snippets (repeatedly promise)
        translations (delay (map translate (strings->sentences (map deref snippets))))]
    (new-session {:snippets snippets :translations translations})))

(defn accept-snippet [session n text]
  (deliver (nth (:snippets session) n) text))

(defn get-translation [session n]
  @(nth @(:translations session) n))

(defroutes app-routes
  (POST "/session/create" []
        (response (str (create-session))))
  (context "/session/:session-id" [session-id]
           (let [session (get-session (edn/read-string session-id))]
             (routes
              (PUT "/snippet/:n" [n :as {:keys [body]}]
                   (accept-snippet session (edn/read-string n) (slurp body))
                   (response "OK"))
              (GET "/translation/:n" [n]
                   (response (get-translation session (edn/read-string n))))))))

(defn -main [& args]
  (run-jetty (wrap-charset (api app-routes)) {:port 3000}))
