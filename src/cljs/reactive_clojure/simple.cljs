(ns reactive-clojure.simple
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [reactive-clojure.utils :as utils]
            [cljs.core.async :refer [put! chan <! onto-chan to-chan pipe]]))

(def marbles (atom {:input (utils/identifiable [{:t 10 :l 31}
                                                {:t 30 :l 12}
                                                {:t 80 :l 42}])
                    :output []}))

(defn render []
  (let [input (to-chan (:input @marbles))
        output (chan)]
    (pipe input output)
    (utils/process output marbles)))

(render)

(defn simple-get []
  (sandbox/marble-sandbox
   (sandbox/sandbox "input" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input @marbles))))
   (sandbox/operator "(go-loop [] (<! (chan)) (recur))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
