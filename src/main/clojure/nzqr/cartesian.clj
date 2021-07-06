(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(ns ^{:author "palisades dot lakes at gmail dot com"
      :date "2021-04-23"
      :doc 
      "A sketch of cartesian product sets, elements,
       and functions.

       This is intended as an alternate approach to the examples
       in SDFF section 2.1, 
       which is nominally about \"combinators\".

       I think that section, and perhaps the idea of 'combinator'
       in general, is unnecessarily complicated by the failure
       to think clearly about functions, their domains
       and codomains, and how that relates to functions 
       with multiple arguments and multiple return values.

       An alternative is to take all functions as single input,
       single output. Multiple argument/value calls are 
       considered syntactic sugar (and perhaps compiler hints)
       for functions whose (co)domains are cartesian product
       spaces.

       With this approach, it seems at this point that (almost
       all) combinators can be replaced by compositions 
       with domain mappings and tuple constructors.

       Some issues to be resolved:
       <ul>
       <li> I'm taking cartesian products as non-associative,
            that is, (AxB)xC != Ax(BxC) != AxBxC, which is clearer
            in lisp syntax: 
            <code>(product (product A B) C)</code>,
            <code>(product A (product B C))</code>,
            <code>(product A  B C)</code>.
            Is this the right choice?
       </li>
       <li> For simplicity in this sketch,
            I'm starting with a single base set:
            all possible Java objects, for short <b>J</b>.
            I'm almost certainly missing some issues 
            because of this, so it needs to be filled in with
            something more realistic before this approach can be
            considered validated in any sense.
       </li>
       <li> Again, for simplicity, all function (co)domains are 
            the copuntable union of all possible cartesian
            product sets built over <b>J</b>.
            In other words,  all domains and codomains are the
            same set, which is a pretty good way to hide problems.
            Let's call this set <b>UJ</b> (better notation?).
            Need to add other sets to expose problems resulting
            from the assumption that all domains are the same.
       </li>
       <li> Elements of <b>UJ</b> will be represented, to begin,
            by Clojure sequences, usually <code>vector</code>s.
            We need to add other representations ot expose 
            problems caused this unconscious assumption.
       </li>
       <li> Tuple constructors are still multi-arity,
            so they aren't actually functions in the 
            single domain, codomain sense.
            Is there a better way to think about this?
       </li>
       <li> Binary operations (associative or not) are common.
            Is there a better way to incorporate them into
            a conceptual 1-in 1-out conceptual model for cuntions?
       </li>
       <li> Multi-arity. multi-multi-value functions
            will require possibly countable unions of
            cartesian product spaces as (co)domains.
            Is there a better way to think about this?
       </li>
       <li> Some of the combinators look like map-reduce 
            transducers. Can we come up with a unified way 
            to talk about both, maybe better than either?
       </li>
       </ul>
       " 
      }
    
    nzqr.cartesian
  
  (:require [clojure.pprint :as pp]
            [clojure.string :as s])
  (:import [clojure.lang IFn])
  )
;;----------------------------------------------------------------

(defn compose 
  "Returns a named function which evaluates the composition
   of <code>f</code> and <code>g</code>.

   <b>TODO:</b> this is a 2-in 1-out function, violating
   1-in, 1-out model we are working towards!
   A variadic arity might fix that?

   <b>TODO:</b> replace with an implementation
   of <code>clojure.lang.IFn</code> that lets us get at the
   <code>factors</code>.

   <b>TODO:</b> this should be a generic function. 
  "
  
  {:added "2021-04-23"}
  
  (^IFn [^IFn f ^IFn g] (fn [x] (f (g x)))))

;;----------------------------------------------------------------

(defn tuple 
  
  "Collect the <code>elements</code> into a cartesian tuple.
   Note that any given tuple may be an element of many
   cartesian product sets.
   Here just using <code>vector</code> for all tuples,
   supporting only the set of all possible Java objects 
   as the element domains.

   <b>TODO:</b> generalize to key-value structure with 
   any reasonable index set.
   "
  
  [& elements] 
  
  (vec elements))

;; TODO: memoize this?

(defn projection 
  
  "Return the projection that selects the <code>i</code>th
   element of a cartesian tuple.

   <b>TODO:</b> handle <code>int</code> indexes, and
   any reasonable index set.


   <b>TODO:</b> Memoize? So we don't need the instances below?
   "
  [i]
  (fn [tuple] (get tuple i)))

(defn split
  "Take 2 functions with the same domain 
   and return a function that maps that
   to the cartesian product of the codomains 
   by applying both function to the input 
   and making a tuple of the 2 results.

   <b>TODO:</b> better name.
   "
  [f g]
  (fn [x] (tuple (f x) (g x))))

;; p 26

(defn parallel-split 
  
  "Compose <code>h</code> with the [[split]] of
   <code>f</code> and <code>g</code>.
   In other words, return a function that first applies both
   <code>f0</code> and <code>f1</code> to the input,
   and then applies <code>h</code> to the output.
   
   <b>TODO:</b> figure out a way to avoid constructing a tuple
   of the values of <code>f0</code> and <code>f1</code>.

   <b>TODO:</b> figure out a way to avoid constructing a tuple
   of the values of <code>f0</code> and <code>f1</code>.

   <b>TODO:</b> better name.

   <b>TODO:</b> Isn't this really just a map-reduce transducer
   where we are mapping over a sequence of functions all applied
   to the same value?.
   "
  [h f0 f1]
  (compose h (split f0 f1)))

(defn diagonal 
  "Take 2 functions and return a function that maps the 
   cartesian product of the domains to the cartesian product
   of the codomains by applying each function to its corresponding
   domain element in the input 
   and making a tuple of the 2 results."
  [f g]
  ;; same projection fns used for diagonals
  (let [p0 (projection 0)
        p1 (projection 1)]
    (fn [x] 
      (tuple
        (f (p0 x)) 
        (g (p1 x))))))

(defn parallel-diagonal
  "Like [[parallel-split]],
   but assumes <code>(domain h)</code> is the cartesian product
   of the codomains of <code>f</code> and <code>g</code>,
   and the domain of the returned function is the cartesian 
   product of the domains of <code>f</code> and <code>g</code>."
  [h f g]
  (compose h (diagonal f g)))

