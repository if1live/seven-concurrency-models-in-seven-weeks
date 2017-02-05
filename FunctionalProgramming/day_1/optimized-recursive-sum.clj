;; 클로저는 다른 많은 함수 언어와는 달리 꼬리-재귀 최적화를 제공하지 않는다.
;; 따라서 실전의 클로저 코드는 재귀를 사용하는 일이 거의 없다.
;;  앞에서 보았던 recursive-sum 함수를 클로저의 loop와 recur를 이용해서 다시 작성하라

(defn recursive-sum [numbers]
    (if (empty? numbers)
        0
        (+ (first numbers) (recursive-sum (rest numbers)))))

(defn recursive-sum-optimized [numbers]
    (loop [acc 0, curr (first numbers), remain (rest numbers)]
        (if (nil? curr)
            acc
            (recur (+ acc curr) (first remain) (rest remain)))))

;; stack overflow
; (println (recursive-sum (range 0 10000)))
(println (recursive-sum-optimized (range 0 10000)))
