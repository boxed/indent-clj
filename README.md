# indent-clj

[![Build Status](https://travis-ci.org/boxed/indent-clj.svg?branch=master)](https://travis-ci.org/boxed/indent-clj)

This is an experiment into Clojure with inferred parenthesis. Files with the extension .indent-clj/.indent-cljs/.indent-cljx are rewritten into normal clojure by adding the inferred parenthesis. Since this is done as a leiningen plugin the rewriting should be fairly transparent so you can work in the indent-clj files only and still compile and test normally. The goal isn't to replace parenthesis, just read/write less of them, more specifically to stop making your brain ignore those that end up at the end of forms as "))))))))))" :P

The parser is super simple so there are going to be some cases that it can't handle, like comments on the line after where the end paren is supposed to go.

## Example:

```clojure
defn expand-path-once [state path]
  let [index-of-star (s/index-of path STAR)]
    if (= index-of-star -1)
      [path]
      let [[path-base path-rest] (split-at-exclusive index-of-star path)]
        if (map? (get-in state path-base))
          let [ks (keys (get-in state path-base))]
            for [k ks]
              (into (into path-base [k]) path-rest)
          []
```

is expanded into:

```clojure
(defn expand-path-once [state path]
  (let [index-of-star (s/index-of path STAR)]
    (if (= index-of-star -1)
      [path]
      (let [[path-base path-rest] (split-at-exclusive index-of-star path)]
        (if (map? (get-in state path-base))
          (let [ks (keys (get-in state path-base))]
            (for [k ks]
              (into (into path-base [k]) path-rest)))
          [])))))
```


## Usage

Put:

[![Clojars Project](http://clojars.org/indent-clj/latest-version.svg)](http://clojars.org/indent-clj)

into the `:plugins` vector of your project.clj, and then leiningen will support indent-clj files.

## License

Copyright © 2014 Anders Hovmöller

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
