(def my-agent (agent 0))
;; 책에서는 아래의 코드를 쓴다
;; (send my-agent #((Thread/sleep 2000) (inc %)))
;; 직접 해보니까 잘 안되는거같아서 함수를 직접 만들어서 붙였다
(defn wait-and-inc [x]
  (Thread/sleep 2000)
  (inc x))
(send my-agent wait-and-inc)
(println @my-agent)

(Thread/sleep 2500)
(println @my-agent)


(def my-agent (agent 0))
;; (send my-agent #((Thread/sleep 2000) (inc %)))
(send my-agent wait-and-inc)
(println (await my-agent))
(println @my-agent)
