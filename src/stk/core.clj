(ns stk.core)

(defn stk-fn [f n]
  (fn [s]
    (let [args (take n s)
          n-s (drop n s)]
      (conj n-s
            (apply f (reverse args))))))

(defn rank [x]
  (cond
    (seqable? x) (+ 1 (rank (first x)))
    :else 0))

(defn dim2 [x]
  (if (seqable? x)
    (cons (count x) (dim2 (first x)))
    (list 0)))

(defn dim [x]
  (butlast (dim2 x)))

(defn broadcast [f x]
  (if (seqable? x)
    (map (fn [e] (broadcast f e)) x)
    (f x)))

(defn stk-replicate [x n]
  (for [_ (range n)]
    x))

(defn stk-apply [f x y]
  (if (and (seqable? x) (seqable? y))
    (if (= (count x) (count y))
      (map f x y)
      (throw (IllegalArgumentException. "Counts do not match")))
    (f x y)))

(defn rank-polymorphic [f]
  (fn [x y]
    (let [r1 (rank x)
          r2 (rank y)]
      (if (= r1 r2)
        (stk-apply f x y)
        (let [mx (max r1 r2)
              mn (min r1 r2)]
          (if (and (> mx 0) (= mn 0))
            (if (= r1 0)
              (broadcast (fn [e] (f x e)) y)
              (broadcast (fn [e] (f e y)) x))
            (throw (IllegalArgumentException. "Ranks do not match"))))))))

(def plus (stk-fn (rank-polymorphic +) 2))
(def minus (stk-fn (rank-polymorphic -) 2))
(def mult (stk-fn (rank-polymorphic *) 2))
(def div (stk-fn (rank-polymorphic /) 2))
(def pow (stk-fn (rank-polymorphic (fn [x y] (Math/pow x y))) 2))

(defn dup [s]
  (let [x (take 1 s)
        n-s (drop 1 s)]
    (conj
      (conj n-s (first x))
      (first x))))

(defn swap [s]
  (let [x (take 2 s)
        n-s (drop 2 s)]
    (concat (reverse x) n-s)))

(defn rot [s]
  (let [[x y z] (take 3 s)
        n-s (drop 3 s)]
    (concat (list y z x) n-s)))

(defn . [s]
  (drop 1 s))

(defn stk-eval [col s env]
  (let [t (first col)]
    (cond
      (nil? t) s
      (fn? t) (recur (rest col) (t s) env)
      (map? t) (recur
                 (rest col)
                 s
                 (merge env t))
      (env t) (recur (concat (env t) (rest col)) s env)
      :else (recur (rest col) (conj s t) env))))
