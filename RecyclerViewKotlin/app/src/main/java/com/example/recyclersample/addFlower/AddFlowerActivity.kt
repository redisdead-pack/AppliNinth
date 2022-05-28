/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample.addFlower

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclersample.R
import com.google.android.material.textfield.TextInputEditText

const val FLOWER_NAME = "name"
const val FLOWER_DESCRIPTION = "description"

class AddFlowerActivity : AppCompatActivity() {
    private lateinit var addFlowerName: TextInputEditText
    private lateinit var addFlowerDescription: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_flower_layout)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addFlower()
        }



        addFlowerName = findViewById(R.id.add_flower_name)
        addFlowerDescription = findViewById(R.id.add_flower_description)
    }




    /* The onClick action for the done button. Closes the activity and returns the new flower name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */

    private fun addFlower() {
        val resultIntent = Intent()

        if (addFlowerName.text.isNullOrEmpty() || addFlowerDescription.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addFlowerName.text.toString()
            val description = addFlowerDescription.text.toString()
            resultIntent.putExtra(FLOWER_NAME, name)
            resultIntent.putExtra(FLOWER_DESCRIPTION, description)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }




}




class HungarianAlgorithm(matrix: Array<IntArray>) {
    var matrix // initial matrix (cost matrix)
            : Array<IntArray>

    // markers in the matrix
    var squareInRow: IntArray
    var squareInCol: IntArray
    var rowIsCovered: IntArray
    var colIsCovered: IntArray
    var staredZeroesInRow: IntArray

    /**
     * find an optimal assignment
     *
     * @return optimal assignment
     */
    fun findOptimalAssignment(): Array<IntArray?> {
        step1() // reduce matrix
        step2() // mark independent zeroes
        step3() // cover columns which contain a marked zero
        while (!allColumnsAreCovered()) {
            var mainZero = step4()
            while (mainZero == null) {      // while no zero found in step4
                step7()
                mainZero = step4()
            }
            if (squareInRow[mainZero[0]] == -1) {
                // there is no square mark in the mainZero line
                step6(mainZero)
                step3() // cover columns which contain a marked zero
            } else {
                // there is square mark in the mainZero line
                // step 5
                rowIsCovered[mainZero[0]] = 1 // cover row of mainZero
                colIsCovered[squareInRow[mainZero[0]]] = 0 // uncover column of mainZero
                step7()
            }
        }
        val optimalAssignment = arrayOfNulls<IntArray>(matrix.size)
        for (i in squareInCol.indices) {
            optimalAssignment[i] = intArrayOf(i, squareInCol[i])
        }
        return optimalAssignment
    }

    /**
     * Check if all columns are covered. If that's the case then the
     * optimal solution is found
     *
     * @return true or false
     */
    private fun allColumnsAreCovered(): Boolean {
        for (i in colIsCovered) {
            if (i == 0) {
                return false
            }
        }
        return true
    }

    /**
     * Step 1:
     * Reduce the matrix so that in each row and column at least one zero exists:
     * 1. subtract each row minima from each element of the row
     * 2. subtract each column minima from each element of the column
     */
    private fun step1() {
        // rows
        for (i in matrix.indices) {
            // find the min value of the current row
            var currentRowMin = Int.MAX_VALUE
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] < currentRowMin) {
                    currentRowMin = matrix[i][j]
                }
            }
            // subtract min value from each element of the current row
            for (k in 0 until matrix[i].size) {
                matrix[i][k] -= currentRowMin
            }
        }

        // cols
        for (i in 0 until matrix[0].size) {
            // find the min value of the current column
            var currentColMin = Int.MAX_VALUE
            for (j in matrix.indices) {
                if (matrix[j][i] < currentColMin) {
                    currentColMin = matrix[j][i]
                }
            }
            // subtract min value from each element of the current column
            for (k in matrix.indices) {
                matrix[k][i] -= currentColMin
            }
        }
    }

    /**
     * Step 2:
     * mark each 0 with a "square", if there are no other marked zeroes in the same row or column
     */
    private fun step2() {
        val rowHasSquare = IntArray(matrix.size)
        val colHasSquare = IntArray(matrix[0].size)
        for (i in matrix.indices) {
            for (j in matrix.indices) {
                // mark if current value == 0 & there are no other marked zeroes in the same row or column
                if (matrix[i][j] == 0 && rowHasSquare[i] == 0 && colHasSquare[j] == 0) {
                    rowHasSquare[i] = 1
                    colHasSquare[j] = 1
                    squareInRow[i] = j // save the row-position of the zero
                    squareInCol[j] = i // save the column-position of the zero
                    continue  // jump to next row
                }
            }
        }
    }

    /**
     * Step 3:
     * Cover all columns which are marked with a "square"
     */
    private fun step3() {
        for (i in squareInCol.indices) {
            colIsCovered[i] = if (squareInCol[i] != -1) 1 else 0
        }
    }

    /**
     * Step 7:
     * 1. Find the smallest uncovered value in the matrix.
     * 2. Subtract it from all uncovered values
     * 3. Add it to all twice-covered values
     */
    private fun step7() {
        // Find the smallest uncovered value in the matrix
        var minUncoveredValue = Int.MAX_VALUE
        for (i in matrix.indices) {
            if (rowIsCovered[i] == 1) {
                continue
            }
            for (j in 0 until matrix[0].size) {
                if (colIsCovered[j] == 0 && matrix[i][j] < minUncoveredValue) {
                    minUncoveredValue = matrix[i][j]
                }
            }
        }
        if (minUncoveredValue > 0) {
            for (i in matrix.indices) {
                for (j in 0 until matrix[0].size) {
                    if (rowIsCovered[i] == 1 && colIsCovered[j] == 1) {
                        // Add min to all twice-covered values
                        matrix[i][j] += minUncoveredValue
                    } else if (rowIsCovered[i] == 0 && colIsCovered[j] == 0) {
                        // Subtract min from all uncovered values
                        matrix[i][j] -= minUncoveredValue
                    }
                }
            }
        }
    }

    /**
     * Step 4:
     * Find zero value Z_0 and mark it as "0*".
     *
     * @return position of Z_0 in the matrix
     */
    private fun step4(): IntArray? {
        for (i in matrix.indices) {
            if (rowIsCovered[i] == 0) {
                for (j in 0 until matrix[i].size) {
                    if (matrix[i][j] == 0 && colIsCovered[j] == 0) {
                        staredZeroesInRow[i] = j // mark as 0*
                        return intArrayOf(i, j)
                    }
                }
            }
        }
        return null
    }

    /**
     * Step 6:
     * Create a chain K of alternating "squares" and "0*"
     *
     * @param mainZero => Z_0 of Step 4
     */
    private fun step6(mainZero: IntArray) {
        var i = mainZero[0]
        var j = mainZero[1]
        val K: MutableSet<IntArray> = LinkedHashSet()
        //(a)
        // add Z_0 to K
        K.add(mainZero)
        var found = false
        do {
            // (b)
            // add Z_1 to K if
            // there is a zero Z_1 which is marked with a "square " in the column of Z_0
            found = if (squareInCol[j] != -1) {
                K.add(intArrayOf(squareInCol[j], j))
                true
            } else {
                false
            }

            // if no zero element Z_1 marked with "square" exists in the column of Z_0, then cancel the loop
            if (!found) {
                break
            }

            // (c)
            // replace Z_0 with the 0* in the row of Z_1
            i = squareInCol[j]
            j = staredZeroesInRow[i]
            // add the new Z_0 to K
            found = if (j != -1) {
                K.add(intArrayOf(i, j))
                true
            } else {
                false
            }
        } while (found) // (d) as long as no new "square" marks are found

        // (e)
        for (zero in K) {
            // remove all "square" marks in K
            if (squareInCol[zero[1]] == zero[0]) {
                squareInCol[zero[1]] = -1
                squareInRow[zero[0]] = -1
            }
            // replace the 0* marks in K with "square" marks
            if (staredZeroesInRow[zero[0]] == zero[1]) {
                squareInRow[zero[0]] = zero[1]
                squareInCol[zero[1]] = zero[0]
            }
        }

/*        // (f)
        // remove all marks
        Arrays.fill(staredZeroesInRow, -1)
        Arrays.fill(rowIsCovered, 0)
        Arrays.fill(colIsCovered, 0)*/
    }

    init {
        if (matrix.size != matrix[0].size) {
            try {
                throw IllegalAccessException("The matrix is not square!")
            } catch (ex: IllegalAccessException) {
                System.err.println(ex)
                System.exit(1)
            }
        }
        this.matrix = matrix
        squareInRow = IntArray(matrix.size) // squareInRow & squareInCol indicate the position
        squareInCol = IntArray(matrix[0].size) // of the marked zeroes
        rowIsCovered = IntArray(matrix.size) // indicates whether a row is covered
        colIsCovered = IntArray(matrix[0].size) // indicates whether a column is covered
        staredZeroesInRow = IntArray(matrix.size) // storage for the 0*
/*        Arrays.fill(staredZeroesInRow, -1)
        Arrays.fill(squareInRow, -1)
        Arrays.fill(squareInCol, -1)*/
    }
}