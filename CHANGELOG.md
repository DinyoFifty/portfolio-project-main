# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## 2026.24.4

### Added

- Designed test suite for GroceryPickupTest and GroceryPickup1LTest component
- Designed two different use cases for GroceryPickupManager and GroceryPickupEmployee component

### Updated

- Changed design to include test cases for grocery pickup and use cases
## 2026.04.15

### Added

- Designed kernel implementation for GroceryPickup component

### Updated

- GroceryPickup1L file to include add, remove, set/get Status, get location, removeAny, then implemented the three standard methods that were inherited.

## 2026.4.1

### Added

- Designed abstract class for GroceryPickup component


### Updated

- Changed design to include subsitute,isOrderComplete,markOutOfStock, and getPickingPath
- Updated GroceryPickupKernel to include a removeAny() method for ease of transfering
- Included toString and equals as key object methods

## 2026.3.10

### Added

- Designed kernel and enhanced interfaces for GroceryPickup component

### Updated

- Changed design to include a STATUS enum, add, remove, setStatus, getStatus, getLocation, and size for kernel methods
- Changed design to include subsitute,isOrderComplete,markOutOfStock, and getPickingPath for secondary methods.

## 2026.02.26

### Added

- Designed a proof of concept for Grocery Pickup component

### Updated

- Changed design to include Status Check, adding or removing items, updating the status of an item, find the aisle location of said item, checking how many items came with the order, if customers wanted a subsitution, when the order is completed, grouping pending items by their aisle location, and marking items out of stock

- Might change the subsitute feature since it assumes the subsituted item is found in the same aisle and shelf

## 2026.02.06

### Added

- Designed a Playable Character<!-- insert name of component 1 here --> component
- Designed a Dashboard component
- Designed a Grocery Pickup component
```


[unreleased]: https://github.com/jrg94/portfolio-project/compare/v2024.08.07...HEAD
[2024.08.07]: https://github.com/jrg94/portfolio-project/compare/v2024.01.07...v2024.08.07
[2024.01.07]: https://github.com/jrg94/portfolio-project/releases/tag/v2024.01.07
