#!/bin/bash

echo "Welcome to the interactive program!"
read -p "Please enter your name: " name
echo "Hello, $name!"

read -p "Please enter a number: " num1
read -p "Please enter another number: " num2
sum=$((num1 + num2))
echo "The sum of $num1 and $num2 is: $sum"