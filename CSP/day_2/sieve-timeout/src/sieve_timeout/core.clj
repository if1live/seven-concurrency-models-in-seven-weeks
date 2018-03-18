(ns sieve-timeout.core
  (:require [clojure.core.async :as async :refer :all
             :exclude [map into reduce merge partition partition-by take]]
            [clojure.edn :as edn]))

(defn factor? [x y]
  (zero? (mod y x)))

(defn get-primes []
  (let [primes (chan)
        numbers (to-chan (iterate inc 2))]
    (go-loop [ch numbers]
      (when-let [prime (<! ch)]
        (>! primes prime)
        (recur (remove< (partial factor? prime) ch)))
      (close! primes))
    primes))


(defn -main [seconds]
  (let [primes (get-primes)
        limit (timeout (* (edn/read-string seconds) 1000))]
    (loop []
      (alt!! :priority true
        limit nil
        primes ([prime] (println prime) (recur))))))
