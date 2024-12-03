package main

import (
	"fmt"
	"io"
	"os"
	"sync"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Println("Usage: go run main.go filepath1 filepath2 filepathN")
		os.Exit(1)
	}

	fileFingerprints := make(map[string][]int64)
	var mutex sync.Mutex
	var barrier sync.WaitGroup

	for _, path := range os.Args[1:] {
		barrier.Add(1)
		go func(path string) {
			defer barrier.Done()
			fingerprint, err := fileSum(path)
			if err != nil {
				fmt.Printf("Error processing file %s: %v\n", path, err)
				os.Exit(1)
			}
			mutex.Lock()
			fileFingerprints[path] = fingerprint
			mutex.Unlock()
		}(path)
	}

	barrier.Wait()

	for i := 0; i < len(os.Args)-2; i++ {
		for j := i + 1; j < len(os.Args)-1; j++ {
			file1 := os.Args[i+1]
			file2 := os.Args[j+1]
			fingerprint1 := fileFingerprints[file1]
			fingerprint2 := fileFingerprints[file2]
			similarityScore := similarity(fingerprint1, fingerprint2)
			fmt.Printf("Similarity between %s and %s: %.2f%%\n", file1, file2, similarityScore*100)
		}
	}
}

func fileSum(filePath string) ([]int64, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var chunks []int64
	buffer := make([]byte, 100)
	for {
		n, err := file.Read(buffer)
		if err == io.EOF {
			break
		}
		if err != nil {
			return nil, err
		}
		chunks = append(chunks, sum(buffer[:n]))
	}

	return chunks, nil
}

func sum(buffer []byte) int64 {
	var total int64
	for _, b := range buffer {
		total += int64(b)
	}
	return total
}

func similarity(base, target []int64) float64 {
	count := 0
	targetCopy := make([]int64, len(target))
	copy(targetCopy, target)

	for _, value := range base {
		for i, t := range targetCopy {
			if t == value {
				count++
				targetCopy = append(targetCopy[:i], targetCopy[i+1:]...)
				break
			}
		}
	}

	return float64(count) / float64(len(base))
}
