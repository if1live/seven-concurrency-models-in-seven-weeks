(ns reducers.core
    (:require [clojure.core.protocols :refer [CollReduce coll-reduce]]
              [clojure.core.reducers :refer [CollFold coll-fold]]))


(def tmp-1 (map (partial * 2) [1 2 3 4]))
(println tmp-1)

(require '[clojure.core.reducers :as r])
(def tmp-2 (r/map (partial * 2) [1 2 3 4]))
(println tmp-2)

;; conj
;; (conj [1] 2)
;; => [1 2]
(def tmp-3 (reduce conj [] (r/map (partial * 2) [1 2 3 4])))
(println tmp-3)

(def tmp-4 (into [] (r/map (partial * 2) [1 2 3 4])))
(println tmp-4)

(def tmp-5 (into [] (r/map (partial + 1) (r/filter even? [1 2 3 4]))))
(println tmp-5)


;(defprotocol CollReduce
;    (coll-reduce [coll f] [coll f init]))

(defn my-reduce
    ([f coll] (coll-reduce coll f))
    ([f init coll] (coll-reduce coll f init)))

(println (my-reduce + [1 2 3 4]))
(println (my-reduce + 10 [1 2 3 4]))

(defn make-reducer [reducible transform]
    (reify
        CollReduce
        (coll-reduce [_ f1]
            (coll-reduce reducible (transform f1) (f1)))
        (coll-reduce [_ f1 init]
            (coll-reduce reducible (transform f1) init))))

(defn my-map [mapf reducible]
    (make-reducer reducible
        (fn [reducef]
            (fn [acc v]
                (reducef acc (mapf v))))))
(println (into [] (my-map (partial * 2) [1 2 3 4])))
(println (into [] (my-map (partial + 1) [1 2 3 4])))
(println (into [] (my-map (partial * 2) (my-map (partial + 1) [1 2 3 4]))))


;(defprotocol CollFold
;    (coll-fold [coll n combinef reducef]))
(defn my-fold
    ([reducef coll]
        (my-fold reducef reducef coll))
    ([combinef reducef coll]
        (my-fold 512 combinef reducef coll))
    ([n combinef reducef coll]
        (coll-fold coll n combinef reducef)))
(defn make-reducer [reducible transform]
    (reify
        CollFold
        (coll-fold [_ n combinef reducef]
            (coll-fold reducible n combinef (transform reducef)))

        CollReduce
        (coll-reduce [_ f1]
            (coll-reduce reducible (transform f1) (f1)))
        (coll-reduce [_ f1 init]
            (coll-reduce reducible (transform f1) init))))
(def v (into [] (range 10000)))
(println (my-fold + v))
(println (my-fold + (my-map (partial * 2) v)))


;; example 1
;; my-map에서 사용하는 방식에 따라 my-flatten과 my-mapcat을 작성하라.
;; 이러한 함수들은 단일한 입력 열을 결과 열의 여러 개의 요소로 확장해야 하기 때문에
;; my-map보다 복잡하다는 점에 유의하기 바란다.
;; 방법이 떠오르지 않으면 부록의 소스 코드를 확인하라.

;;(flatten x)
;; Takes any nested combination of sequential things (lists, vectors,
;; etc.) and returns their contents as a single, flat sequence.
;; (flatten nil) returns an empty sequence.
(println (flatten [1 [2 3]]))

(defn my-flatten [reducible]
    (make-reducer reducible
        (fn [reducef]
            (fn [acc v]
                (if (sequential? v)
                    (coll-reduce (my-flatten v) reducef acc)
                    (reducef acc v))))))

(println (into [] (my-flatten [1 [2 3]])))

;; (mapcat f)(mapcat f & colls)
;; Returns the result of applying concat to the result of applying map
;; to f and colls.  Thus function f should return a collection. Returns
;; a transducer when no collections are provided
(println (mapcat reverse [[3 2 1 0] [6 5 4] [9 8 7]]))

(defn my-mapcat [mapf reducible]
    (make-reducer reducible
        (fn [reducef]
            (fn [acc v]
                (if (sequential? v)
                    (coll-reduce (my-flatten (mapf v)) reducef acc)
                    (reducef acc (mapf v)))))))

(println (into [] (my-mapcat reverse [[3 2 1 0] [6 5 4] [9 8 7]])))


;; example 2
;; my-filter를 작성하라.
;; 입력 열에 있는 여러 요소를 축소해야 하기 때문에 my-map보다 복잡할 것이다.

;; (filter pred)(filter pred coll)
;; Returns a lazy sequence of the items in coll for which
;; (pred item) returns true. pred must be free of side-effects.
;; Returns a transducer when no collection is provided.
(println (filter even? (range 10)))

(defn my-filter [filterf reducible]
    (make-reducer reducible
        (fn [reducef]
            (fn [acc v]
                (if (filterf v)
                    (reducef acc v)
                    acc)))))

(println (into [] (my-filter even? (range 10))))
