This repository contains code related to entanglement entropy calculations in lattice gauge theory.

Programs:

###### LinkVsPhysEE

* calculates and plots the entanglement entropy of physical states of the form `a|--> + b|++>` in a 2-plaquette Z<sub>2</sub> lattice gauge theory. It also plots the EE using a link-based splitting of the space in which the right plaquette belongs to one region and the remaining links to its complement.
* The basic result is fairly boring: the physical EE varies as it would in a 2-spin state, while the link EE is constant:

<img src="https://raw.githubusercontent.com/hategan/ee/master/linkee-physee-2p-z2.png" width="300px"/>

## Usage

You need a recent version of Java and Apache Ant.

To compile the code, run `ant jar` in the main directory. This should produce `lib/ee.jar`. To run tests:

```bash
cd bin
./runtests
```

To run link-vs-phys-ee:

```bash
cd bin
./link-vs-phys-ee
```
