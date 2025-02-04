/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 1;
        showValue = false;
        left = null;
        right = null;
    }

    private int max(int first, int second) {
        return (first > second) ? first : second;
    }

    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private TreeNode rightRotation(TreeNode node) {
        TreeNode leftNode = node.left;
        TreeNode rightLeftNode = leftNode.right;
        leftNode.right = node;
        node.left = rightLeftNode;
        node.height = 1 + this.max(this.getHeight(node.left), this.getHeight(node.right));
        leftNode.height = 1 + this.max(this.getHeight(leftNode.left), this.getHeight(leftNode.right));
        return leftNode;
    }

    private TreeNode leftRotation(TreeNode node) {
        TreeNode rightNode = node.right;
        TreeNode leftRightNode = rightNode.left;
        rightNode.left = node;
        node.right = leftRightNode;
        node.height = 1 + this.max(this.getHeight(node.left), this.getHeight(node.right));
        rightNode.height = 1 + this.max(this.getHeight(rightNode.left), this.getHeight(rightNode.right));
        return rightNode;
    }

    private int getBalanceFactor(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return this.getHeight(node.left) - this.getHeight(node.right);
    }

    private TreeNode _insert(TreeNode current, int value) {
        if (current == null) {
            return new TreeNode(value);
        }
        if (value < current.value) {
            current.left =  this._insert(current.left, value);
        }
        else if (value > current.value) {
            current.right =  this._insert(current.right, value);
        }
        else {
            return current;
        }
        current.height = 1 + this.max(this.getHeight(current.left), this.getHeight(current.right));
        int balanceFactor = this.getBalanceFactor(current);
        if (balanceFactor > 1 && value < current.left.value) {
            return this.rightRotation(current);
        }
        if (balanceFactor < -1 && value > current.right.value) {
            return this.leftRotation(current);
        }
        if (balanceFactor > 1 && value > current.left.value) {
            current.left = this.leftRotation(current.left);
            return this.rightRotation(current);
        }
        if (balanceFactor < -1 && value < current.right.value) {
            current.right = this.rightRotation(current.right);
            return this.leftRotation(current);
        }
        return current;
    }

    public void insert(int value) {
        if (value < this.value) {
            this.left = _insert(this.left, value);
        }
        else {
            this.right = _insert(this.right, value);
        }
    }

    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
