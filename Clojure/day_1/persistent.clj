(def mapv1 {:name "paul" :age 45})
(def mapv2 (assoc mapv1 :sex :male))
(println mapv1)
(println mapv2)

(def listv1 (list 1 2 3))
(println listv1)

(def listv2 (cons 4 listv1))
(println listv2)

(def listv3 (cons 5 (rest listv1)))
(println listv3)

(def listv1 (list 1 2 3 4))
(def listv2 (take 2 listv1))
(println listv2)
