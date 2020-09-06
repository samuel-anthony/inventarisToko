<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateRiwayatBahanPokoksTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('riwayat_bahan_pokoks', function (Blueprint $table) {
            $table->bigIncrements('riwayat_bahan_pokok_id');
            $table->tinyInteger('aksi')->default(0);//nilai 0 untuk pengambilan, nilai 1 untuk penambahan kuantitas
            $table->integer('jumlah');//jumlah yang diambil/ditambah, jika ditambah wajib menambahkan data supplier
            $table->integer('harga')->default(0);//dalam rupiah, default 0 karena jika pengambilan tidak melakukan pengeluaran
            $table->unsignedBigInteger('bahan_pokok_id');
            $table->unsignedBigInteger('supplier_id')->nullable();
            $table->foreign('bahan_pokok_id')->references('bahan_pokok_id')->on('bahan_pokoks');
            $table->foreign('supplier_id')->references('supplier_id')->on('suppliers');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('riwayat_bahan_pokoks');
    }
}
