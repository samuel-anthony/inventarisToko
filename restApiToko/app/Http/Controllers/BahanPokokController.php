<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\bahanPokok;
use App\riwayatBahanPokok;
use App\supplier;
class BahanPokokController extends Controller
{
    //
    public function index(){
        return json_encode(["result"=>bahanPokok::all()]);
    }

    public function addBahanPokokBaru(request $request){
        $bahanPokok = new bahanPokok;
        $bahanPokok->nama = $request->nama;
        if(!is_null($request->jumlah))
            $bahanPokok->jumlah = $request->jumlah;
        $bahanPokok->satuan = $request->satuan;
        $bahanPokok->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function addRiwayatBahanPokok(request $request){
        $riwayatBahanPokok = new riwayatBahanPokok;
        
        $riwayatBahanPokok->aksi = $request->aksi; //expect nilai 0, dan 1... 0 untuk pengambilan, 1 untuk penambahan
        $riwayatBahanPokok->jumlah = $request->jumlah;
        if(!is_null($request->aksi) && $request->aksi == 1)//karena bydefault aksi == null adalah pengambilan
            $riwayatBahanPokok->harga = $request->harga;
        $riwayatBahanPokok->bahan_pokok_id = $request->bahan_pokok_id;
        if(!is_null($request->supplier_id)){
            $riwayatBahanPokok->supplier_id = $request->supplier_id;
            $supplier = supplier::find($request->supplier_id) ? supplier::find($request->supplier_id) : new supplier;
            $supplier->nama = $request->nama_supplier;
            $supplier->alamat = $request->alamat_supplier;
            $supplier->nomor_telepon = $request->nomor_telepon_supplier;
            $supplier->save();
        }     
        $riwayatBahanPokok->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }
}
