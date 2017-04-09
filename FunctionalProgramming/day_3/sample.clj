;; future usage
(def sum (future (+ 1 2 3 4 5)))
(println sum)
(println (deref sum))
(println @sum)

(println
 (let [a (future (+ 1 2))
       b (future (+ 3 4))]
   (+ @a @b)))

;; promise usage
(def meaning-of-life (promise))
(future (println "The meaning of life is: " @meaning-of-life))
(deliver meaning-of-life 42)
