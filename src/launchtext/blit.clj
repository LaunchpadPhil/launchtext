(ns launchtext.blit "Convert a string into a Bitmap")

(use 'matchure)

; Convert a piece of text into a vector of columns

(def letters
{
\A [[2 1] [3 2] [4 3] [4 4] [3 4] [4 5] [1 2] [2 4] [4 6] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\a [[3 2] [4 3] [2 2] [4 4] [4 5] [1 2] [3 5] [4 6] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\B [[2 1] [3 2] [1 1] [2 3] [3 4] [0 1] [2 4] [3 5] [0 2] [1 3] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6] ]
\b [[2 3] [3 4] [0 1] [3 5] [0 2] [1 3] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6] ]
\C [[2 1] [1 1] [0 2] [3 6] [0 3] [2 6] [0 4] [0 5] [1 6] [3 1]]
\c [[2 2] [3 3] [1 2] [3 5] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\D [[2 1] [3 2] [3 3] [1 1] [3 4] [0 1] [3 5] [0 2] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6] ]
\d [[3 1] [3 2] [3 3] [2 3] [3 4] [3 5] [1 3] [3 6] [2 6] [0 4] [0 5] [1 6] [3 1]]
\E [[2 1] [3 3] [1 1] [2 3] [0 1] [0 2] [1 3] [3 6] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6] [3 1]]
\e [[3 2] [2 2] [3 3] [1 2] [2 4] [3 6] [0 3] [1 4] [2 6] [0 4] [0 5] [1 6] ]
\F [[2 1] [3 3] [1 1] [2 3] [0 1] [0 2] [1 3] [0 3] [0 4] [0 5] [0 6] [3 1]]
\f [[3 2] [2 2] [3 4] [1 2] [2 4] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\G [[2 1] [1 1] [3 4] [2 4] [3 5] [0 2] [3 6] [0 3] [2 6] [0 4] [0 5] [1 6] [3 1]]
\g [[2 1] [3 2] [3 3] [1 1] [2 3] [3 4] [3 5] [0 2] [1 3] [2 6] [0 5] [1 6] ]
\H [[3 2] [3 3] [2 3] [3 4] [0 1] [3 5] [0 2] [1 3] [3 6] [0 3] [0 4] [0 5] [0 6] [3 1]]
\h [[2 4] [3 5] [0 2] [3 6] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\I [[2 1] [1 1] [0 1] [1 2] [1 3] [1 4] [2 6] [1 5] [1 6] [0 6] ]
\i [[0 1] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\J [[2 1] [2 2] [1 1] [2 3] [0 1] [2 4] [2 5] [2 6] [0 5] [1 6] [3 1]]
\j [[3 3] [3 4] [3 5] [2 6] [0 5] [1 6] [3 1]]
\K [[2 2] [0 1] [2 4] [3 5] [0 2] [1 3] [3 6] [0 3] [0 4] [0 5] [0 6]  [3 1]]
\k [[2 4] [0 1] [0 2] [2 6] [0 3] [1 5] [0 4] [0 5] [0 6] ]
\L [[0 1] [0 2] [3 6] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6] ]
\l [[0 2] [0 3] [2 6] [0 4] [0 5] [1 6] [0 1]]
\M [[3 2] [4 3] [4 4] [2 3] [4 5] [0 1] [1 2] [4 6] [0 2] [0 3] [0 4] [0 5] [0 6] [4 1] [4 2]]
\m [[4 3] [3 3] [4 4] [4 5] [2 4] [4 6] [0 2] [1 3] [0 3] [0 4] [0 5] [0 6] ]
\N [[3 2] [3 3] [2 3] [3 4] [0 1] [1 2] [3 5] [0 2] [3 6] [0 3] [0 4] [0 5] [0 6] [3 1]]
\n [[3 3] [3 4] [3 5] [2 5] [3 6] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\O [[2 1] [3 2] [3 3] [1 1] [3 4] [3 5] [0 2] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\o [[2 2] [3 3] [3 4] [1 2] [3 5] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\P [[2 1] [3 2] [3 3] [1 1] [0 1] [2 4] [0 2] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\p [[2 3] [1 2] [0 2] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\Q [[2 1] [3 2] [3 3] [1 1] [3 4] [3 5] [4 6] [0 2] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\q [[3 2] [2 2] [3 3] [3 4] [1 2] [2 4] [3 5] [3 6] [0 3] [1 4] ]
\R [[2 1] [3 2] [3 3] [1 1] [0 1] [2 4] [0 2] [2 5] [3 6] [0 3] [1 4] [0 4] [0 5] [0 6] ]
\r [[3 2] [2 2] [0 2] [1 3] [0 3] [0 4] [0 5] [0 6] ]
\S [[2 1] [1 1] [2 4] [3 5] [0 2] [0 3] [1 4] [2 6] [1 6] [0 6] [3 1]]
\s [[3 2] [2 2] [1 2] [2 4] [3 5] [0 3] [1 4] [2 6] [1 6] [0 6] ]
\T [[2 1] [1 1] [0 1] [1 2] [1 3] [1 4] [1 5] [1 6] [3 1]]
\t [[3 3] [2 3] [1 2] [1 3] [0 3] [1 4] [2 6] [1 5] ]
\U [[3 2] [3 3] [3 4] [0 1] [3 5] [0 2] [0 3] [2 6] [0 4] [0 5] [1 6] [3 1]]
\u [[3 3] [3 4] [3 5] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\V [[3 2] [3 3] [3 4] [0 1] [3 5] [0 2] [0 3] [2 6] [0 4] [1 5] [3 1]]
\v [[3 3] [3 4] [3 5] [0 3] [2 6] [0 4] [1 5] ]
\W [[4 3] [4 4] [4 5] [0 1] [2 4] [3 5] [4 6] [0 2] [0 3] [0 4] [1 5] [0 5] [0 6] [4 1] [4 2]]
\w [[4 3] [4 4] [4 5] [2 4] [3 5] [4 6] [0 3] [0 4] [1 5] [0 5] [0 6] ]
\X [[3 2] [2 3] [0 1] [1 2] [2 4] [3 5] [4 6] [1 5] [0 6] [4 1] ]
\x [[3 3] [2 4] [3 5] [4 6] [0 2] [1 3] [1 5] [0 6] [4 2]]
\Y [[3 3] [0 1] [2 4] [0 2] [1 3] [2 5] [2 6] [4 1] [4 2]]
\y [[3 2] [3 3] [3 4] [0 2] [2 5] [0 3] [1 4] [1 6] [0 6] ]
\Z [[2 1] [3 2] [1 1] [2 3] [0 1] [3 6] [1 4] [2 6] [0 5] [1 6] [0 6] [3 1]]
\z [[3 2] [2 2] [3 3] [1 2] [2 4] [0 2] [3 6] [2 6] [1 5] [1 6] [0 6] ]
\1 [[2 1] [2 2] [2 3] [1 2] [2 4] [2 5] [3 6] [0 3] [2 6] [1 6] [0 6] ]
\2 [[2 1] [3 2] [3 3] [1 1] [2 4] [0 2] [3 6] [0 3] [2 6] [1 5] [1 6] [0 6] ]
\3 [[2 1] [3 2] [3 3] [1 1] [2 4] [3 5] [0 2] [1 4] [2 6] [1 6] [0 6] ]
\4 [[3 2] [2 2] [3 3] [3 4] [2 4] [3 5] [1 3] [3 6] [1 4] [0 4] [3 1]]
\5 [[2 1] [1 1] [3 4] [0 1] [2 4] [3 5] [0 2] [1 3] [3 6] [0 3] [2 6] [1 6] [0 6]  [3 1]]
\6 [[2 1] [1 1] [2 4] [3 5] [0 2] [3 6] [0 3] [1 4] [2 6] [0 4] [0 5] [1 6]  [3 1]]
\7 [[2 1] [3 2] [3 3] [1 1] [0 1] [2 4] [1 5] [0 6] [3 1]]
\8 [[2 1] [3 2] [3 3] [1 1] [2 3] [3 4] [0 1] [3 5] [0 2] [1 3] [3 6] [0 3] [2 6] [0 4] [0 5] [1 6] [0 6]  [3 1]]
\9 [[2 1] [3 2] [3 3] [1 1] [3 4] [2 4] [3 5] [0 2] [3 6] [0 3] [1 4] [3 1]]
\0 [[2 1] [3 2] [3 3] [1 1] [3 4] [2 4] [3 5] [0 2] [1 3] [0 3] [2 6] [0 4] [0 5] [1 6] ]
\. [[1 5] [0 5] [1 6] [0 6] ]
})

(defn- space [as] (mapcat #(vector % []) as))

(defn-match fromNil ([?a nil] a)
                    ([_  ?b ] b))

(defn-match len ([[] ] 0)
                ([?xs] (inc (apply max (map first xs)))))

(defn- l [i bs] (map second (filter #(= i (first %)) bs)))

(defn- letter [c] (let [bits (fromNil [] (letters c))]
                   (map #(l % bits)
                        (range 0 (inc (len bits))))))

(defn blit [text] (mapcat letter text))


; Test functions

(defn- g [i s] (if ((set s) i) "#" " "))

(defn test-blit []
  (doseq [x (blit "Smoke Weed Every Day - Dr. Dre.")]
    (do (doseq [y (reverse (range 0 8))] (print (g y x)))
        (print "\n"))))

(defn-match blit-at ([?b [?x ?y]] (if ((set ((apply vector b) x)) y) 1 0)))
