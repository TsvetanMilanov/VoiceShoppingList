package com.telerik.academy.voiceshoppinglist.utilities;

public final class StringsSimilarityCalculator {
    private static final int DELETE_OPERATION_WEIGHT = 1; // 2
    private static final int REPLACE_OPERATION_WEIGHT = 1; // 3
    private static final int INSERT_OPERATION_WEIGHT = 1; // 3

    /**
     * Calculates the Levenshtein distance between two strings.
     * @param originalString The original String.
     * @param suggestionString The suggestion string.
     * @return Returns the Levenshtein distance.
     */
    private static int CalculateEditDistance(String originalString, String suggestionString) {
        int originalStringLength = originalString.length();
        int suggestionStringLength = suggestionString.length();

        int[][] dp = new int[originalStringLength + 1][suggestionStringLength + 1];

        // Calculate base cases of th dp.
        for (int i = 0; i <= originalStringLength; i++) {
            dp[i][0] = i;
        }

        // Calculate base cases of th dp.
        for (int j = 0; j <= suggestionStringLength; j++) {
            dp[0][j] = j;
        }

        for (int i = 0; i < originalStringLength; i++) {
            char firstCharacter = originalString.charAt(i);
            for (int j = 0; j < suggestionStringLength; j++) {
                char secondCharacter = suggestionString.charAt(j);

                if (firstCharacter == secondCharacter) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + REPLACE_OPERATION_WEIGHT;
                    int insert = dp[i][j + 1] + INSERT_OPERATION_WEIGHT;
                    int delete = dp[i + 1][j] + DELETE_OPERATION_WEIGHT;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[originalStringLength][suggestionStringLength];
    }

    /**
     * Calculates the similarity coefficient between two strings.
     * @param originalString The original string.
     * @param suggestionString The suggestion string.
     * @return Returns the similarity coefficient in range 0 - 100.
     */
    public  static  double calculateSimilarityCoefficient(String originalString, String suggestionString) {
        int distance = CalculateEditDistance(originalString, suggestionString);

        double similarityCoefficient = 1 - (Double.parseDouble(distance + "") / (originalString.length()));

        return similarityCoefficient * 100;
    }
}
