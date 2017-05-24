(def non-negative (agent 1 :validator (fn [new-val] (>= new-val 0))))

(send non-negative dec)
(Thread/sleep 100)
(println @non-negative)

(send non-negative dec)
(Thread/sleep 100)
(println @non-negative)

;; 에러 발생 이후에 호출하는 경우
;; (send non-negative inc)
;; (Thread/sleep 100)
;; (println @non-negative)

;; 에러 처리
(println (agent-error non-negative))

(restart-agent non-negative 0)
(println (agent-error non-negative))

(send non-negative inc)
(Thread/sleep 100)
(println @non-negative)
