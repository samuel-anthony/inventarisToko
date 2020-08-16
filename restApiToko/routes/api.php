<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

//get request
Route::get('getPesananBelumSelesai','PesananController@getAllTodayUnfinishedOrder');
Route::get('bahanPokok','BahanPokokController@index');


Route::post('testApi','UserController@testApi');
Route::post('register','UserController@register');
Route::post('login','UserController@loginAdmin');
Route::post('loginQR','UserController@loginCustomer');
