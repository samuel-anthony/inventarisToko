<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\jenisMenu;
use App\jenisMenuDetail;
use App\makanan;

class JenisMenuController extends Controller
{
    public function index(){
        return json_encode(["result"=>jenisMenu::all()]);
    }

    public function jenisMenuDetailByid(request $request){
        $jenisMenu = jenisMenu::find($request->jenis_menu_id);
        $temp = array();
        foreach($jenisMenu->jenisMenuDetails as $detail){
             
            array_push($temp, $detail->makanan);
        }
        $jenisMenu->makanan = $temp;
        return json_encode(["result"=>$jenisMenu]);
    }
}
