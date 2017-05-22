(def my-atom (atom 42))
(println (deref my-atom))
(println @my-atom)

(swap! my-atom inc)
(println @my-atom)

(swap! my-atom + 2)
(println @my-atom)

(reset! my-atom 0)
(println @my-atom)

(def session (atom {}))
(swap! session assoc :uername "paul")
(swap! session assoc :session-id 1234)
(println @session)
