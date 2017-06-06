2(ns philosophers.core
  (:require [philosophers.util :refer :all]))

(def philosophers (atom (into [] (repeat 5 :thinking))))

(defn think []
  (Thread/sleep (rand 1000)))

(defn eat []
  (Thread/sleep (rand 1000)))

(defn claim-chopsticks! [philosopher left right]
  (swap-when! philosophers
    #(and (= (%1 left) :thinking) (= (%1 right) :thinking))
    assoc philosopher :eating))

(defn release-chopsticks! [philosopher]
  (swap! philosophers assoc philosopher :thinkging))

(defn philosopher-thread [philosopher]
  (Thread.
   #(let [left (mod (- philosopher 1) 5)
          right (mod (+ philosopher 1) 5)]
      (while true
        (think)
        (when (claim-chopsticks! philosopher left right)
          (println "eat start......" philosopher "left=" left "right=" right)
          (eat)
          (println "eat finished..." philosopher "left=" left "right=" right)
          (release-chopsticks! philosopher))))))

(defn -main [& args]
  (let [threads (map philosopher-thread (range 5))]
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))
