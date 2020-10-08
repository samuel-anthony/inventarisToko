<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\makanan;
use App\makananDetail;
use App\bahanPokok;
use App\gambarMakanan;
class MakananController extends Controller
{
    //

    public function index(){
        return json_encode(['result'=>makanan::all()]);
    }

    public function makananByDetail(request $request){
        $makanan = makanan::find($request->makanan_id);
        $temp = array();
        $temp2 = array();
        foreach($makanan->makananDetails as $detail){
            $bahanPokok = $detail->bahanPokok;
            $bahanPokok->jumlah = $detail->jumlah;
            array_push($temp,$bahanPokok);
            array_push($temp2, $bahanPokok->bahan_pokok_id);
        }
        $makanan->bahanPokoks = $temp;
        return json_encode(['result'=>$makanan,"bahanPokokYangBelumMasuk"=>bahanPokok::whereNotIn('bahan_pokok_id',$temp2)->get()]);
    }
    
    public function getGambarMakananByDetail(request $request){
        $gambar = gambarMakanan::whereMakananId($request->makanan_id)->first();
        return  is_null($gambar) ? null : $gambar->gambar_makanan;
    }


    public function addNewMakanan(request $request){
        $makanan = new makanan;
        $makanan->nama = $request->nama_makanan;
        $makanan->harga_jual = $request->harga_jual;
        $makanan->save();
        if(!is_null($request->gambar_makanan)){
            $gambarMakanan = new gambarMakanan;
            $gambarMakanan->makanan_id = $makanan->makanan_id;
            $gambarMakanan->gambar_makanan = $request->gambar_makanan;
            $gambarMakanan->save();
        }
        $bahanMakanan = json_decode($request->bahanMakanan);
        foreach($bahanMakanan as $detail){
            $makananDetail = new makananDetail;
            $makananDetail->jumlah = $detail->jumlah;
            $makananDetail->bahan_pokok_id = $detail->bahan_pokok_id;
            $makananDetail->makanan_id = $makanan->makanan_id;
            $makananDetail->save();
        }
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function editMakanan(request $request){
        $makanan =  makanan::find($request->makanan_id);
        $makanan->nama = $request->nama_makanan;
        $makanan->harga_jual = $request->harga_jual;
        $gambarMakanan = is_null($makanan->gambarMakanan) ? new gambarMakanan : $makanan->gambarMakanan;
        if(strcmp($request->gambar_makanan,"")){
            $gambarMakanan->gambar_makanan = $request->gambar_makanan;
            $gambarMakanan->makanan_id = $makanan->makanan_id;
            $gambarMakanan->save();
        }
        $makanan->save();
        $bahanMakanan = json_decode($request->bahanMakanan);
        $usedmakananDetail = array();
        foreach($bahanMakanan as $detail){
            $makananDetail = makananDetail::whereMakananId($request->makanan_id)->whereBahanPokokId($detail->bahan_pokok_id)->first();
            if(is_null($makananDetail))
                $makananDetail = new makananDetail;
            $makananDetail->jumlah = $detail->jumlah;
            $makananDetail->bahan_pokok_id = $detail->bahan_pokok_id;
            $makananDetail->save();
            array_push($usedmakananDetail,$makananDetail->makanan_detail_id);
        }
        $makananDetails = makananDetail::whereMakananId($request->makanan_id)->whereNotIn('makanan_detail_id',$usedmakananDetail)->get();
        foreach($makananDetails as $detail){
            $detail->delete();
        }
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function deleteMakanan(request $request){
        $makanan =  makanan::find($request->makanan_id);
        foreach($makanan->makananDetails as $detail){
            $detail->delete();
        }
        $makanan->delete();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }
}
