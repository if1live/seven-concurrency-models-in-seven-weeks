(ns server.core
  (:require [ring.util.response :refer [response status]]
            [compojure.core :refer :all]
            [compojure.handler  :refer [site]]
            [ring.adapter.jetty :refer [run-jetty]]
            [cheshire.core :as json]))

(def players (atom ()))

(defn list-players []
  (response (json/encode @players)))

(defn create-player [player-name]
  (swap! players conj player-name)
  (status (response "") 201))

;; 4.2.2절에서 보았던 TournamentServer 예제를 수정해서 선수가 추가되는 것만이 아니라
;; 삭제되는 것도 가능하게 만들어라.
(defn remove-player [player-name]
  (letfn [(equal-player-name [name] (= player-name name))]
    (swap! players #(remove equal-player-name %))
    (status (response "") 202)))

(defroutes app-routes
  (GET "/players" [] (list-players))
  (PUT "/players/:player-name" [player-name] (create-player player-name))
  (DELETE "/players/:player-name" [player-name] (remove-player player-name)))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
