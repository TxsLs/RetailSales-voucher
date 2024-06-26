import json from '@rollup/plugin-json';
import resolve from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import {babel} from '@rollup/plugin-babel';
import terser from '@rollup/plugin-terser';
import eslint from '@rollup/plugin-eslint';


import pkg from './package.json'

const year = new Date().getFullYear()
const banner = `/*!
 * bootstrap-admin v${pkg.version} (${pkg.homepage})
 * Copyright 2021-${year} ${pkg.author}
 * license ${pkg.license} (https://gitee.com/ajiho/bootstrap-admin/blob/2.x/LICENSE)
 */`


export default [
  {
    input: 'build/js/bootstrap-admin.js',
    output: [
      {
        banner,
        file: 'dist/js/bootstrap-admin.js',
        format: 'umd',
        globals: {
          jquery: 'jQuery',
          'bootstrap-quicktab': 'Quicktab',
        },
        name: 'BootstrapAdmin',
        sourcemap: true,
      },
      {
        banner,
        file: 'dist/js/bootstrap-admin.min.js',
        format: 'umd',
        globals: {
          jquery: 'jQuery',
          'bootstrap-quicktab': 'Quicktab',
        },
        name: 'BootstrapAdmin',
        plugins: [
          terser({compress: {drop_console: false}})
        ],
        sourcemap: true,
      }
    ],
    external: ['jquery', 'bootstrap-quicktab'],
    //使用json插件
    plugins: [
      json(),
      resolve(),
      commonjs(),
      eslint({
        throwOnError: true,
        throwOnWarning: true,
        include: ['build/**'],
        exclude: ['node_modules/**'],
        fix: true
      }),
      babel({
        exclude: 'node_modules/**',
        // 用于指定 Babel 在转换 ES6+ 代码时使用的辅助函数的路径
        babelHelpers: 'bundled'
      })
    ]
  },
  //mock打包
  {
    input: 'build/mock/index.js',
    output: [
      {
        file: 'dist/js/bootstrap-admin.mock.js',
        format: 'iife',
        globals: {
          jquery: 'jQuery'
        },
        sourcemap: false, // 不生成 sourcemap 文件
      }
    ],
    external: ['jquery'],
    plugins: [
      json(),
      resolve(),
      commonjs(),
      eslint({
        throwOnError: true,
        throwOnWarning: true,
        include: ['build/**'],
        exclude: ['node_modules/**'],
        fix: true
      }),
      babel({
        exclude: 'node_modules/**',
        // 用于指定 Babel 在转换 ES6+ 代码时使用的辅助函数的路径
        babelHelpers: 'bundled'
      }),
      terser({compress: {drop_console: false}})
    ],
    onwarn: function (message) {//避免打包mock.js的时候会报错
      if (message.code === 'EVAL') {
        return;
      }
      console.error(message);
    }
  },
]
