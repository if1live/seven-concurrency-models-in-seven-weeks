(def a (atom 0))

(add-watch a :print #(println "Changed from" %3 " to " %4))
(swap! a + 2)
