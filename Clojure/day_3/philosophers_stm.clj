(def philosophers (into [] (repeatedly 5 #(ref :thinking))))

(defn think []
  (Thread/sleep (rand 1000)))

(defn eat []
  (Thread/sleep (rand 1000)))

;; (defn claim-chopsticks [philosopher left right]
;;   (dosync
;;    (when (and (= @left :thinking) (= @right :thinking))
;;      (ref-set philosopher :eating))))

;; 리턴하는 ref의 값이 다른 트랜잭션에 의해 변경되지 않았음을 보장하기위해 ensure 사용
(defn claim-chopsticks [philosopher left right]
  (dosync
   (when (and (= (ensure left) :thinking) (= (ensure right) :thinking))
     (ref-set philosopher :eating))))


(defn release-chopsticks [philosopher]
  (dosync (ref-set philosopher :thinking)))

(defn philosopher-thread [n]
  (Thread.
   #(let [philosopher (philosophers n)
          left (philosophers (mod (- n 1) 5))
          right (philosophers (mod (+ n 1) 5))
          left-idx (mod (- n 1) 5)
          right-idx (mod (+ n 1) 5)]
      (while true
        (think)
        (when (claim-chopsticks philosopher left right)
          (println "eat start......" n "left=" left-idx "right=" right-idx)
          (eat)
          (println "eat finished..." n "left=" left-idx "right=" right-idx)
          (release-chopsticks philosopher))))))


(defn -main [& args]
  (let [threads (map philosopher-thread (range 5))]
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))

(-main)
