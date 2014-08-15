(ns leiningen.indent-clj)

(defn indent-clj
  [project & args]
  (println "Hi!"))

; TODO: handle comment lines: insert paren before the comment

(def indent-size 2)
(def indent-str "  ")

(defmacro for-v [binding-forms & body]
  `(into [] (for [~@binding-forms] ~@body)))

(defn line-data [line]
  (let [after-indent (clojure.string/trim (or (get (re-find #"(  )*." line) 0) ""))
        indent (/ (count (get (re-find #"(  )*" line) 0)) indent-size)]
    {:line (subs line (* indent indent-size))
     :indent indent
     :eligible-start (and (not= "" after-indent)
                          (not= "(" after-indent))}))

(def empty-line-data
  {:ignore false
   :line ""
   :indent 0
   :eligible-start false})

(defn drop-first-add-bogus-last [line-datas bogus]
  (conj (into [] (drop 1 line-datas)) bogus))

(defn get-next-non-ignored [data start-index]
  (let [x (first (filter #(not (:ignored %)) (drop (+ 1 start-index) data)))]
    (if (nil? x)
      empty-line-data
      x)))

(defmacro for-x-and-next-x [data fill-last-elem & body]
  `(for-v [i# (range (count ~data))]
     (let [~'x (get ~data i#)
           ~'next-x (get-next-non-ignored ~data i#)]
       ~@body)))

(defn set-ignore-flag [line-datas]
  (for-v [i (range (count line-datas))]
    (let [x (get line-datas i)
          indent (get-in line-datas [i :indent])
          prev-indent (get-in line-datas [(- 1 i) :indent])]
      (if (or (.startsWith (:line x) " ") (and prev-indent (> indent (+ 1 prev-indent))))
        (assoc x :ignore true)
        (assoc x :ignore false)))))

(defn fix-eligible-start [line-datas]
  (for-x-and-next-x line-datas empty-line-data
                    (if (:eligible-start x)
                      (prn x next-x))
                    (assoc x :eligible-start (and (> (:indent next-x) (:indent x)) (:eligible-start x)))))

(defn add-start-paren [input]
  (for-v [x input]
    (if (:eligible-start x)
      (assoc x :line (str "(" (:line x)))
      x)))

(defn count-unbalanced-paren [line-datas]
  (for-v [x line-datas]
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
  (for-v [line-data line-datas]
    (assoc line-data :line (str (duplicate-string indent-str (:indent line-data)) (:line line-data)))))

(defn foo [input]
  (let [lines (clojure.string/split input #"\n")]
    (->> (into [] (concat (map line-data lines) [empty-line-data]))
         set-ignore-flag
         fix-eligible-start
         add-start-paren
         count-unbalanced-paren
         add-end-paren
         re-add-indent
         (map :line)
         (clojure.string/join "\n")
         clojure.string/trim
        )))

