(ns reactive-clojure.first
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [reactive-clojure.utils :as utils]
            [cljs.core.async :refer [put! chan <! onto-chan pipe take to-chan]]))

(def marbles (atom {:input [{:t 10 :l 1}
                            {:t 30 :l 2}
                            {:t 80 :l 3}]
                    :output []}))

(defn render []
  (let [input (to-chan (:input @marbles))
        output (take 1 input)]
    (utils/process output marbles)))

(render)

(defn just-first []
  (sandbox/marble-sandbox
   (sandbox/sandbox "input" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input @marbles))))
   (sandbox/operator "(<! (take 1 (chan)))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
