(ns bbb.core
  (:require [clojure.contrib.duck-streams :as ds]
            [clojure.contrib.string :as str]
            [clojure.contrib.shell-out :as so])
  (:import java.io.File))

(defn- filter-output [^File input ^File output]
  (filter #(and (not= (.getName %) "templates")
                (not= (.getName %) "posts")
                (not= (.getName %) (first (str/split #"/" (.getName output)))))
          (.listFiles input)))

(defn- copy-files [files ^File target]
  (prn (map #(so/sh "cp" "-R" (str %) (str target)) files)))

(defn prepare-environment
  ([]
   (prepare-environment (File. ".") (File. "_site")))
  ([^File input ^File output]
   (let [children (filter-output input output)]
     (copy-files children output)
     (prn children)
     (.mkdirs output))))

