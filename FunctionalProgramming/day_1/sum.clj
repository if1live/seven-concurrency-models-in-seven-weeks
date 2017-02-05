(ns sum.core
    (:require [clojure.core.reducers :as r]))

(defn recursive-sum [numbers]
    (if (empty? numbers)
        0
        (+ (first numbers) (recursive-sum (rest numbers)))))

(defn reduce-sum [numbers]
    (reduce (fn [acc x] (+ acc x)) 0 numbers))

(defn sum [numbers]
    (reduce + numbers))

(defn apply-sum [numbers]
    (apply + numbers))

(defn parallel-sum [numbers]
    (r/fold + numbers))

;(def numbers [1 2 3 4 5 6 7 8 9 10])
(def numbers (into [] (range 0 10000000)))

;; stack overflow!
;(time (recursive-sum numbers))

(println "sum")
(time (sum numbers))
(time (sum numbers))

(println "apply-sum")
(time (apply-sum numbers))
(time (apply-sum numbers))

(println "parallel-sum")
(time (parallel-sum numbers))
(time (parallel-sum numbers))

