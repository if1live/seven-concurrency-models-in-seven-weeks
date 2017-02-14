(ns reduces.core
    (:require [clojure.core.reducers :as r]))

(defn parallel-frequencies [coll]
    (r/fold
        (partial merge-with +)
        (fn [counts x] (assoc counts x (inc (get counts x 0))))
        coll))
(take 10 (repeatedly #(rand-int 10)))
(def numbers (into [] (take 10000000 (repeatedly #(rand-int 10)))))

;; for jit
(frequencies numbers)
(time (frequencies numbers))

(parallel-frequencies numbers)
(time (parallel-frequencies numbers))
