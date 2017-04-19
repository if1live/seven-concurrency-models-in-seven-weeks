(ns server.core
  (:require [compojure.core     :refer :all]
            [compojure.handler  :refer [site]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clojure.edn :as edn]))

(def snippets (repeatedly promise))

(defn accept-snippet [n text]
  (deliver (nth snippets n) text))

(future
  (doseq [snippet (map deref snippets)]
    (println snippet)))

(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
       (accept-snippet (edn/read-string n) (slurp body))
       (response "OK")))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
