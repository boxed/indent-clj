(ns leiningen.indent-clj)

(defn indent-clj
  [project & args]
  (println "Hi!"))


(defn line-data [line]
  (let [after-indent (clojure.string/trim (or(get (re-find #"(    )*." line) 0) ""))]
    {:line (clojure.string/trim line)
     :indent (/ (count (get (re-find #"(    )*" line) 0)) 4)
     :eligible-start (if (= after-indent "")
                       false
                       (not (contains? #{"("} after-indent)))}))

(def empty-line-data
  {:indent 0, :eligible-start false, :line ""})

(defn drop-first-add-bogus-last [line-datas bogus]
  (conj (into [] (drop 1 line-datas)) bogus))

(defmacro for-x-and-next-x [data fill-last-elem & body]
  `(for [[~'x ~'next-x] (map vector ~data (drop-first-add-bogus-last ~data ~fill-last-elem))]
     ~@body))

(defn fix-eligible-start [line-datas]
  (for-x-and-next-x line-datas empty-line-data
                    (assoc x :eligible-start (and (> (:indent next-x) (:indent x)) (:eligible-start x)))))

(defn add-start-paren [input]
  (for [x input]
    (if (:eligible-start x)
      (assoc x :line (str "(" (:line x)))
      x)))

(defn count-unbalanced-paren [line-datas]
  (for [x line-datas]
    (assoc x :unbalanced-paren (count (clojure.string/replace (:line x) #"[^(]" "")))))

(defn duplicate-string [s times]
  (clojure.string/join (for [_ (range times)] s)))

(defn add-end-paren [line-datas]
  (for-x-and-next-x line-datas empty-line-data
                    (if (< (:indent next-x) (:indent x))
                      (assoc x :line (str (:line x)
                                          (duplicate-string ")" (- (:indent x) (:indent next-x)))))
                      x)))

(defn re-add-indent [line-datas]
  (for [line-data line-datas]
    (assoc line-data :line (str (duplicate-string "    " (:indent line-data)) (:line line-data)))))

(defn foo [input]
  (let [lines (clojure.string/split input #"\n")]
    (->> (concat (map line-data lines) [empty-line-data])
         fix-eligible-start
         add-start-paren
         count-unbalanced-paren
         add-end-paren
         re-add-indent
         (map :line)
         (clojure.string/join "\n")
         clojure.string/trim
        )))
