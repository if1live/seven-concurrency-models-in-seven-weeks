(def my-agent (agent 0))
(println @my-agent)

(send my-agent inc)
;; send는 별도 쓰레드에서 실행되니
;; 수정된것이 즉시 보이지 않을수도 있다
;; 그래서 sleep
(Thread/sleep 100)
(println @my-agent)

(send my-agent + 2)
(Thread/sleep 100)
(println @my-agent)
