<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\supplier;
class SupplierController extends Controller
{
    public function index(){
        return json_encode(["result"=>supplier::all()]);
    }
}
