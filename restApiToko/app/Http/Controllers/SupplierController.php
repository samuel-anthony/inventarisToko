<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\supplier;
class SupplierController extends Controller
{
    public function index(){
        return json_encode(["result"=>supplier::all()]);
    }

    public function addSupplier(request $request){
        $supplier = new supplier;
        $supplier->nama = $request->nama_supplier;
        $supplier->alamat = $request->alamat_supplier;
        $supplier->nomor_telepon = $request->nomor_telepon_supplier;
        $supplier->save();
        
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

}
