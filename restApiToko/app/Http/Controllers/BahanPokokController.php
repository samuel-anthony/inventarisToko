<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\bahanPokok;

class BahanPokokController extends Controller
{
    //
    public function index(){
        return bahanPokok::all();
    }
}
