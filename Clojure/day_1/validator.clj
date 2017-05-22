(def non-negative (atom 0 :validator #(>= % 0)))

(reset! non-negative 42)
(println @non-negative)

(reset! non-negative -1)
(println @non-negative)
