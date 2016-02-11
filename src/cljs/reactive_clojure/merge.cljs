(ns reactive-clojure.merge
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [reactive-clojure.utils :as utils]
            [cljs.core.async :refer [put! chan <! to-chan merge pipe]]))

(def marbles (atom {:input-a [{:t 10 :l 1}
                              {:t 30 :l 2}
                              {:t 80 :l 3}]
                    :input-b [{:t 20 :l \X}
                              {:t 50 :l \Y}]
                    :output []}))

(defn render []
  (let [input-a (to-chan (:input-a @marbles))
        input-b (to-chan (:input-b @marbles))
        output  (merge [input-a input-b])]
    (utils/process output marbles)))

(render)

(defn merged []
  (sandbox/marble-sandbox
   (sandbox/sandbox "inputA" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input-a @marbles))))
   (sandbox/sandbox "inputB" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input-b @marbles))))
   (sandbox/operator "(<! (merge [(chan) (chan)]))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))
