(defn get-words [text] (re-seq #"\w+" text))

(def pages ["one potato two potato three potato four"
            "five potato six potato seven potato more"])
;(println (pmap #(frequencies (get-words %)) pages))

(def merge-counts (partial merge-with +))
;(println (merge-counts {:x 1 :y 2} {:y 1 :z 1}))

(defn count-words-parallel [pages]
    (reduce (partial merge-with +)
        (pmap #(frequencies (get-words %)) pages)))
(println (count-words-parallel pages))


(defn count-words-sequential [pages]
    (frequencies (mapcat get-words pages)))

(defn count-words [pages]
    (reduce (partial merge-with +)
        (pmap count-words-sequential (partition-all 100 pages))))
;(println (partition-all 4 (range 1 11)))
(println (count-words pages))

(System/exit 0)
