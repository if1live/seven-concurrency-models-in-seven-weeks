(ns transfer.core)

(defn transfer [from to amount]
  (dosync
   (alter from - amount)
   (alter to + amount)))

(def checking (ref 1000))
(def savings (ref 2000))

(transfer savings checking 100)
(println @checking)
(println @savings)


(def attempts (atom 0))
(def transfers (agent 0))

(defn transfer [from to amount]
  (dosync
   ;; 의도된 에러
   ;; atom은 트랜잭션이 복구해주지 않는다
   (swap! attempts inc)

   (send transfers inc)
   (alter from - amount)
   (alter to + amount)))

(def checking (ref 10000))
(def savings (ref 20000))

(defn stress-thread [from to iterations amount]
  (Thread. #(dotimes [_ iterations] (transfer from to amount))))

(defn -main [& args]
  (println "Before: Checking =" @checking " Savings =" @savings)
  (let [t1 (stress-thread checking savings 100 100)
        t2 (stress-thread savings checking 200 100)]
    (.start t1)
    (.start t2)
    (.join t1)
    (.join t2)
    (await transfers)
    (println "Attempts: " @attempts)
    (println "Transfers: " @transfers)
    (println "After: Checking =" @checking " Savings =" @savings)))
