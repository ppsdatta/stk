(ns stk.core)

(defn stk-fn [f n]
  (fn [s]
    (let [args (take n s)
          n-s (drop n s)]
      (conj n-s
            (apply f (reverse args))))))

(def plus (stk-fn + 2))
(def minus (stk-fn - 2))
(def mult (stk-fn * 2))
(def div (stk-fn / 2))
(def pow (stk-fn (fn [x y] (Math/pow x y)) 2))


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
