(defn word-frequencies [words]
    (reduce
        (fn [counts word] (assoc counts word (inc (get counts word 0))))
        {} words))

(println (word-frequencies ["one" "potato" "two" "potato" "three" "potato" "four"]))
(println (frequencies ["one" "potato" "two" "potato" "three" "potato" "four"]))

(println (map inc [0 1 2 3 4 5]))
(println (map (fn [x] (* 2 x)) [0 1 2 3 4 5]))

(def multiply-by-2 (partial * 2))
(println (multiply-by-2 3))
(println (map (partial * 2) [0 1 2 3 4 5]))

(defn get-words [text] (re-seq #"\w+" text))
(println (get-words "one two three four"))

(println (map get-words ["one two three" "four five six" "seven eight nine"]))
(println (mapcat get-words ["one two three" "four five six" "seven eight nine"]))

(defn count-words-sequential [pages]
    (frequencies (mapcat get-words pages)))

(def pages [
    "one potato two potato three potato four"
    "one two three"
])
(println (count-words-sequential pages))

(println (take 10 (range 0 10000000)))
(println (take 10 (map (partial * 2) (range 0 10000000))))

(println (take 10 (iterate inc 0)))
(println (take 10 (iterate (partial + 2) 0)))

(println (take-last 5 (range 0 10000000)))
