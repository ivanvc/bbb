(ns bbb.core
  (:require [clojure.contrib.duck-streams :as ds]
            [clojure.contrib.string :as str])
  (:import java.io.File))

(defn filter-output [^File input ^File output]
  (filter #(and (not= (.getName %) "templates")
                (not= (.getName %) "posts")
                (not= (.getName %) (first (str/split #"/" (.getName output)))))
          (.listFiles input)))

(defn prepare-environment
  ([]
   (prepare-environment (File. ".") (File. "_site")))
  ([^File input ^File output]
   (let [children (filter-output input output)]
     (prn children)
     (.mkdirs output))))

