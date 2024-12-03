package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"sync"
)

func readFile(filePath string) ([]byte, error) {
	data, err := ioutil.ReadFile(filePath)
	if err != nil {
		fmt.Printf("Error reading file %s: %v", filePath, err)
		return nil, err
	}
	return data, nil
}

func sum(filePath string) (int, error) {
	data, err := readFile(filePath)
	if err != nil {
		return 0, err
	}

	_sum := 0
	for _, b := range data {
		_sum += int(b)
	}

	return _sum, nil
}

func worker(filePath string, barrier *sync.WaitGroup, totalSumChan chan int64, sumMapChan chan map[int][]string) {
	defer barrier.Done()

	_sum, err := sum(filePath)
	if err != nil {
		return
	}

	totalSumChan <- int64(_sum)

	sumMap := make(map[int][]string)
	sumMap[_sum] = append(sumMap[_sum], filePath)
	sumMapChan <- sumMap
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Usage: go run main.go <file1> <file2> ...")
		return
	}

	var barrier sync.WaitGroup
	totalSumChan := make(chan int64, len(os.Args)-1)
	sumMapChan := make(chan map[int][]string, len(os.Args)-1)

	for _, path := range os.Args[1:] {
		barrier.Add(1)
		go worker(path, &barrier, totalSumChan, sumMapChan)
	}

	barrier.Wait()
	close(totalSumChan)
	close(sumMapChan)

	var totalSum int64
	sums := make(map[int][]string)

	for ts := range totalSumChan {
		totalSum += ts
	}

	for sm := range sumMapChan {
		for sum, files := range sm {
			sums[sum] = append(sums[sum], files...)
		}
	}

	fmt.Println(totalSum)

	for sum, files := range sums {
		if len(files) > 1 {
			fmt.Printf("Sum %d: %v\n", sum, files)
		}
	}
}
