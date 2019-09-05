#include <vector>
#include <unordered_set>
#include <iostream>
#include <fstream>
#include <cstdint>
#include <stdlib.h>
#include <chrono>
#include "Utility.h"

bool setup(std::vector<int> * const freqs);
void partOne(std::vector<int> * const freqs);
void partTwo(std::vector<int> * const freqs);


int main() {
	std::vector<int> deltaFreqs;

	if (!setup(&deltaFreqs)) {
		return EXIT_FAILURE;
	}

	auto start = std::chrono::high_resolution_clock::now();

	partOne(&deltaFreqs);

	auto finish = std::chrono::high_resolution_clock::now();
	auto elapsedTimeInMilliSeconds = std::chrono::duration_cast<std::chrono::microseconds>(finish - start).count();
	std::cout << "Elapsed Time: " + std::to_string(elapsedTimeInMilliSeconds) + "microseconds";
	start = finish;
	partTwo(&deltaFreqs);

	finish = std::chrono::high_resolution_clock::now();
	elapsedTimeInMilliSeconds = std::chrono::duration_cast<std::chrono::milliseconds>(finish - start).count();
	std::cout << "Elapsed Time: " + std::to_string(elapsedTimeInMilliSeconds) + "ms";

	return EXIT_SUCCESS;
}

bool setup(std::vector<int> * const freqs) {
	std::ifstream inFile;
	inFile.open("Input.txt");
	if (inFile.is_open()) {
		std::string inNum = "0";
		while (std::getline(inFile, inNum)) {
			int convertedNum = std::stoi(inNum);
			freqs->push_back(convertedNum);
		}
		inFile.close();
	} else {
		std::cout << "Could not open the input file";
		return false;
	}
	return true;
}

void partOne(std::vector<int> * const freqs) {
	int finalFreq = 0;
	for (auto x : (*freqs)) {
		finalFreq += x;
	}
	std::cout << "Final frequency is: " + std::to_string(finalFreq);
}

void partTwo(std::vector<int> * const freqs) {
	std::unordered_set<int> unique;
	int freq = 0;

	int i = 0;
	int count = 0;
	int size = freqs->size();

	while (unique.insert(freq).second) {
		freq += freqs->at(i);
		count++;
		i = ++i % size;
	}
	std::cout << "Duplicate frequency found at iteration " + std::to_string(count);
	std::cout << "First duplicate frequency is " + std::to_string(freq);
}
