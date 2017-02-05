;; (fn ...) 대신 #() 리더 매크로를 이용해서 3.2.3절의 reduce-sum을 다시 작성하라.

(defn reduce-sum [numbers]
    (reduce (fn [acc x] (+ acc x)) 0 numbers))

;; http://stackoverflow.com/questions/10069214/nesting-of-in-clojure-macros
(defn reduce-sum-macro [numbers]
    (reduce #(+ %1 %2) 0 numbers))

(println (reduce-sum (range 0 3)))
(println (reduce-sum-macro (range 0 3)))
