(ns server.core
  (:require [ring.util.response :refer [response status]]
            [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :refer [run-jetty]]
            [cheshire.core :as json]))

(def players (atom ()))

(defn list-players []
  (response (json/encode @players)))

(defn create-player [player-name]
  (swap! players conj player-name)
  (status (response "") 201))

;; 4.2.2절에서 보았던 TournamentServer를 수정해서
;; ref와 트랜잭션을 이용해 틱택토(tic-tac-toe) 토너먼트를 수행하는 서버를 구현하라.

(def games (atom {}))

(defn create-game-ctx [player-a player-b]
  {:player-a player-a
  :player-b player-b
  :turn (ref 0)
  :board (ref [nil nil nil nil nil nil nil nil nil])})

(defn encode-game-ctx [game]
  {:player-a (game :player-a)
  :player-b (game :player-b)
  :turn @(game :turn)
  :board @(game :board)})

(defn get-expected-player [game]
  (if (= (mod @(game :turn) 2) 0)
    (game :player-a)
    (game :player-b)))

(defn get-current-piece [game]
  (if (= (mod @(game :turn) 2) 0)
    "o"
    "x"))

(defn can-place-at [game coord-x coord-y]
  (let [idx (+ (* 3 (dec coord-y)) (dec coord-x))]
    (nil? (@(game :board) idx))))

(defn place-at [game coord-x coord-y]
  (let [idx (+ (* 3 (dec coord-y)) (dec coord-x))
        piece (get-current-piece game)]
    (dosync
     (alter (game :turn) inc)
     (ref-set (game :board) (assoc @(game :board) idx piece)))))

(defn create-game [game-name player-a player-b]
  (swap! games conj {game-name (create-game-ctx player-a player-b)})
  (response (json/encode {:game-name game-name})))

(defn play-game-not-your-turn [game-name]
  (response (json/encode {:game-name game-name
                         :mag "not your turn"})))
(defn play-game-cannot-place-at [game-name]
  (response (json/encode {:game-name game-name
                         :mag "already exist"})))

(defn play-game-place-at [game game-name coord-x coord-y]
  (place-at game coord-x coord-y)
  (response (json/encode {:game-name game-name
                         :msg "place success"})))

(defn play-game [game-name player-name coord-x coord-y]
  (let [game (@games game-name)
        expected-player (get-expected-player game)]
    (if (not= player-name expected-player)
      (play-game-not-your-turn game-name)
      (if (not (can-place-at game coord-x coord-y))
        (play-game-cannot-place-at game-name)
        (play-game-place-at game game-name coord-x coord-y)))))


(defn show-game [game-name]
  (response (json/encode (encode-game-ctx (@games game-name)))))


(defroutes app-routes
  (GET "/players" [] (list-players))
  (PUT "/players/:player-name" [player-name] (create-player player-name))

  (PUT "/games/new/:game-name/:player-a/:player-b"
       [game-name player-a player-b]
       (create-game game-name player-a player-b))
  (PUT "/games/play/:game-name/:player-name/:coord-x/:coord-y"
       [game-name player-name coord-x coord-y]
       (play-game game-name player-name (edn/read-string coord-x) (edn/read-string coord-y)))
  (GET "/games/show/:game-name" [game-name] (show-game game-name)))


(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
