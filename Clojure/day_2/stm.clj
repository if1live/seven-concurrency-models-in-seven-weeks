(def my-ref (ref 0))
(println @my-ref)

;; IllegalStateException: No transaction running
;; (ref-set my-ref 42)

;; IllegalStateException: No transaction running
;; (alter my-ref inc)

(dosync (ref-set my-ref 42))
(println @my-ref)

(dosync (alter my-ref inc))
(println @my-ref)
