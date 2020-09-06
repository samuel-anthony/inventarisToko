<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\makanan;
use App\makananDetail;
class MakananController extends Controller
{
    //

    public function index(){
        return json_encode(['result'=>makanan::all()]);
    }
    
    public function addNewMakanan(request $request){
        $makanan = new makanan;
        $makanan->nama = $request->nama_makanan;
        $makanan->harga_jual = $request->harga_jual;
        $makanan->gambar_makanan = $request->gambar_makanan;
        $makanan->save();
        $bahanMakanan = json_decode($request->bahanMakanan);
        foreach($bahanMakanan as $detail){
            $makananDetail = new makananDetail;
            $makananDetail->jumlah = $detail->jumlah;
            $makananDetail->bahan_pokok_id = $detail->bahan_pokok_id;
            $makananDetail->makanan_id = $makanan->id;
            $makananDetail->save();
        }
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }
}
