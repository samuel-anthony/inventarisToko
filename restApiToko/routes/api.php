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
Route::get('getPesananDetailRefNo','PesananController@getOrderByRefNo');
Route::get('bahanPokok','BahanPokokController@index');
Route::get('login','UserController@loginAdmin');
Route::get('loginQR','UserController@loginCustomer');
Route::get('getSemuaDataMeja','UserController@getAllCustomerUser');
Route::get('getSemuaAdminUser','UserController@getAllAdminUser');


Route::post('testApi','UserController@testApi');
Route::post('register','UserController@register');
Route::post('registerAdmin','UserController@registerAdmin');

Route::put('updateStatusPesanan','PesananController@updateStatusRefNo');
Route::put('updateStatusPesananSelesai','PesananController@updateStatusFinishRefNo');
Route::put('updateAdminUser','UserController@updateAdminUser');
Route::put('deleteAdmin','UserController@deleteAdmin');
Route::put('updateUser','UserController@updateUser');
Route::put('deleteUser','UserController@deleteUser');