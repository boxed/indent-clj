(ns indent-clj.test-indent
  [:use midje.sweet]
  [:require [leiningen.indent-clj :refer :all]])

;; The following line is useful for debugging
; (spit "test/test-output.clj" (infer-paren (slurp "test/test.indent-clj")))

(fact
 (infer-paren "foo\n  bar") => "(foo\n  bar)"
 (infer-paren "foo\n  bar\n    baz") => "(foo\n  (bar\n    baz))"
 (clojure.string/trim (infer-paren (slurp "test/test.indent-clj"))) => (clojure.string/trim (slurp "test/test.normal-clj"))
 )
